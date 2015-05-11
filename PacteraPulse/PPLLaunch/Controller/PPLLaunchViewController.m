//
//  PPLLaunchViewController.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLLaunchViewController.h"

@interface PPLLaunchViewController ()

@property (nonatomic, weak) IBOutlet UIImageView *launchImageView;

@end

@implementation PPLLaunchViewController

NSString *const kVoteSegueId = @"showVoteOption";

- (void)viewDidLoad
{
    [super viewDidLoad];
    // The image would be linked with a tap gesture, so clicking anywhere on the screen
    // would take you to
    // Voting screen
    UITapGestureRecognizer *singleFingerTap =
    [[UITapGestureRecognizer alloc] initWithTarget:self
                                            action:@selector(handleSingleTap:)];
    [self.launchImageView addGestureRecognizer:singleFingerTap];
    self.launchImageView.userInteractionEnabled = YES;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)handleSingleTap:(id)sender
{
    [self performSegueWithIdentifier:kVoteSegueId sender:nil];
}

@end
