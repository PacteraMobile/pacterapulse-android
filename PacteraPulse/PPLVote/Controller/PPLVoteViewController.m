//
//  PPLVoteViewController.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLVoteViewController.h"
#import "PPLVoteTableViewCell.h"
#import "PPLNetworkingHelper.h"
#import "PPLVoteData.h"
#import "MBProgressHUD.h"
#import "PPLLaunchViewController.h"
#import "PPLVoteRow.h"
#import "AppDelegate.h"
#import "PPLVoteManagerClass.h"
#import "PPLSummaryBarViewController.h"
static NSString *kViewTitle = @"How Do You Feel Today?";

@interface PPLVoteViewController ()

@property (nonatomic, weak) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *data;

@end

@implementation PPLVoteViewController

NSString *const kSummarySegueId = @"toSummary";
NSString *const kCellId = @"voteCell";
NSString *const kNavigationButtonTitle = @"Info";
NSInteger const kPadding = 10;

enum voteAction
{
    VOTE_NOT_SUBMITTED,
    VOTE_SUBMITTED,
    VOTE_NO_ACTION
} voteState;

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.data = [PPLVoteRow initObjects];
    self.tableView.contentInset = UIEdgeInsetsZero;
    self.title = kViewTitle;
    UIBarButtonItem *rightButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAction target:self action:@selector(showResult:)];
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc] initWithTitle:kNavigationButtonTitle style:UIBarButtonItemStyleDone target:self action:@selector(showLaunch:)];
    self.navigationItem.rightBarButtonItem = rightButton;
    self.navigationItem.leftBarButtonItem = leftButton;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - TableView Datasource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //count number of row from counting array here category is An Array
    return self.data.count;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = NO;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    PPLVoteTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellId];

    PPLVoteRow *item = self.data[indexPath.row];
    [cell setUpRow:item];
    [cell.emotionButton addTarget:self action:@selector(handleClick:) forControlEvents:UIControlEventTouchUpInside];
    return cell;
}

/**
 *  Tap gesture to submit the feedback to the server and go to summary screen on success
 *
 *  @param recognizer singleTao gesture
 */
- (void)handleClick:(id)sender
{
    UIView *senderView = (UIView *)sender;
    NSInteger sourceTag = senderView.tag;

    PPLVoteManagerClass *voteManager = [PPLVoteManagerClass sharedInstance];
    
    if ([voteManager checkIfVoteSubmittedToday])
    {
        voteState = VOTE_NOT_SUBMITTED;
        [self performSegueWithIdentifier:kSummarySegueId sender:nil];

    }
    else
    {
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        //Send the feedback via data model, we provide two callbacks one for success and one for failure
        [[PPLVoteData shareInstance] sendFeedback:[NSString stringWithFormat:@"%ld", (long)sourceTag]
                                         callBack:^(BOOL status, NSString* serverResponse, NSError *error)
         {
             if (status) {
                 voteState = VOTE_SUBMITTED;
                 [voteManager recordVoteSubmission:[NSString stringWithFormat:@"%ld", (long)sourceTag]];
                 [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                 [self performSegueWithIdentifier:kSummarySegueId sender:nil];
             }
             else
             {
                 UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                 message:@"Issue submitting feedback to server"
                                                                delegate:self
                                                       cancelButtonTitle:@"OK"
                                                       otherButtonTitles:nil];
                 [alert show];
                 [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
             }
             
         }];
    }
}

#pragma mark -TableViewDelegate
- (CGFloat)tableView:(UITableView*)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}

/**
 *  We need to create the row sizes so we only show the required options on the screens
 *  we calculate dynamically the size based on total images we have
 */
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    screenHeight -= self.navigationController.navigationBar.frame.size.height;
    return (screenHeight/self.data.count - kPadding);
}

#pragma mark - Navigation
- (void)showResult:(id)sender
{
    voteState = VOTE_NO_ACTION;
    [self performSegueWithIdentifier:kSummarySegueId sender:nil];
}
/**
 *  This function shows the launch screen which has instructions
 *
 *  @param sender Initiator for this function
 */
- (void)showLaunch:(id)sender
{
    // Get all the View Controllers from this Navigation Controllers
    NSArray *navigationViews = self.navigationController.viewControllers;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:kStoryboardId bundle:nil];
    // First of all check if Voting screen is the first view controller or not
    // because if Vote controller is the root view controller it means the application
    // was not launched for the rist time and at initialization the voting screen
    // became the root controller for this view.
    if (navigationViews[0] == self)
    {
        // Initialise the launch view controller, this will be inserted in the navigation controller
        UIViewController *initViewController = [storyboard instantiateViewControllerWithIdentifier:kLaunchScreen];
        // Initialize a temporary array with all view controllers
        NSMutableArray *tempArray = [[NSMutableArray alloc] initWithArray:navigationViews];
        // Insert the newly created controller as the first element of array of
        // view controllers
        [tempArray insertObject:initViewController atIndex:0];
        // Set this newly arranged Array of view controllers as the views managed
        // by the current navigation controller, which means the newly created
        // launch controller is at the root now.
        [self.navigationController setViewControllers:tempArray];
    }
    
    // After all the modifications (if done) go to the root controller
    [self.navigationController popToRootViewControllerAnimated:YES];
}
/**
 *  Before showing the summary graph we need to show the destination controller
 *  if on this screen the vote was submitted or not, or if its directly
 *  navigating to the summary without voting, so it can show the alert
 *  accordingly
 *
 *  @param segue  segue for finding destination
 *  @param sender initiator of this segue
 */
-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:kSummarySegueId]) {
        
        PPLSummaryBarViewController *destinationController =
        (PPLSummaryBarViewController*)segue.destinationViewController;
        
        //Check the current vote state and set the variable for destination
        switch (voteState) {
            case VOTE_NOT_SUBMITTED:
                destinationController.shouldShowAlert = YES;
                break;
            case VOTE_NO_ACTION:
            case VOTE_SUBMITTED:
                destinationController.shouldShowAlert = NO;
                break;
        }
    }
    
}

@end
