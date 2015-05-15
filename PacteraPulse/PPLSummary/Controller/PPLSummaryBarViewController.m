//
//  PPLSummaryBarViewController.m
//  PacPulse
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLSummaryBarViewController.h"
#import "PPLColoredBarChart.h"
#import "PPLSummaryData.h"
#import "PPLSummaryGenerals.h"
#import "MBProgressHUD.h"
#import "CSNotificationView.h"

@interface PPLSummaryBarViewController ()

@property (nonatomic, strong) CPTGraphHostingView * hostingView;
@property (nonatomic, strong) PPLColoredBarChart *barItem;
@property (nonatomic, strong) NSArray *summaryData;

@end

@implementation PPLSummaryBarViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self showVotedNotification];
    [self setTitle:sPPLSummaryTilte];
    [self configureHost];
    [self fetchRemoteData];
}

- (void)showVotedNotification
{
    if(self.shouldShowAlert )
    {
        [CSNotificationView showInViewController:self
                                       style:CSNotificationViewStyleError
                                     message:sPPLSummaryVoteAgainAlert];
    }
}

- (void)configureHost
{
    CGRect parentRect = self.view.bounds;
    parentRect.origin.y += iTitleSpace;
    parentRect.size.height -= iTitleSpace;
    self.hostingView = [(CPTGraphHostingView *)[CPTGraphHostingView alloc]initWithFrame:parentRect];
    self.hostingView.allowPinchScaling = YES;
    [self.view addSubview:self.hostingView];
    self.hostingView.layer.transform = CATransform3DMakeRotation(M_PI, 1, 0, 0);
}

- (void)fetchRemoteData
{
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    [[PPLSummaryData shareInstance]  emotionValues:@"24hours" callBack:^(BOOL status, NSArray *graphValues, NSError *error)
    {
        if (status && error == nil && graphValues != nil && [graphValues count] > 0) {
            
            NSMutableArray* updatedData = [NSMutableArray arrayWithArray:graphValues];
            float totalCount = 0;
            //Calculating total count
            NSInteger arrCount = [graphValues count];
            for(int i = 0; i < arrCount; i++)
            {
                totalCount += [[[updatedData objectAtIndex:i] valueForKey:kEmotionValue] integerValue];
            }
    
            //Calculating percentage from each emotion
            for(int i = 0; i < arrCount; i++)
            {
                float percentage =  0;
                if( totalCount != 0)
                {
                    percentage = [[[updatedData objectAtIndex:i] valueForKey:kEmotionValue] floatValue]* iMaxPercent /totalCount;
                }
                [[updatedData objectAtIndex:i] setObject:[NSNumber numberWithFloat:percentage] forKey:kEmotionValue];
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                    [self configureBarView:updatedData];
                });
        }
        else
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                            message:@"Can't get feedback from server, please kindly try later."
                                                           delegate:self
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
            alert = nil;
        }
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
    }];
}

- (void)configureBarView:(NSArray*)fetchedData
{
    self.summaryData = fetchedData;
    self.barItem = [[PPLColoredBarChart alloc]init];
    self.barItem.summaryData = self.summaryData;
    [self.barItem renderInView:self.hostingView withTheme:nil animated:YES];
}

- (void)reloadGraph
{
    self.barItem.summaryData = self.summaryData;
    [self.barItem renderInView:self.hostingView withTheme:nil animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

@end
