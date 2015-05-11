//
//  PacteraPulseTests.m
//  PacteraPulseTests
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "AppDelegate.h"
#import "PPLLaunchViewController.h"
#import "PPLVoteViewController.h"
@interface PPLLaunchTests : XCTestCase

@end

@implementation PPLLaunchTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testCheckFirstLaunchMarked
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    if ([prefs boolForKey:kLauchFirstTime])
    {
        [prefs removeObjectForKey:kLauchFirstTime];
    }
    [delegate markFirstLaunch];
    XCTAssertFalse([delegate checkFirstLaunch]);
}

- (void)testSkipLaunchScreenOnSecondLaunch
{
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    if ([delegate checkFirstLaunch])
    {
        XCTAssertTrue([[delegate selectRootViewController] isKindOfClass:[PPLLaunchViewController class]]);
    }
    else
    {
        XCTAssertTrue([[delegate selectRootViewController] isKindOfClass:[PPLVoteViewController class]]);
    }
}

@end
