//
//  PacteraPulseTests.m
//  PacteraPulseTests
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "PPLSummaryData.h"
#import "PPLSummaryBarViewController.h"
#import "CSNotificationView.h"


@interface PPLSummaryTests : XCTestCase

@end

@implementation PPLSummaryTests

- (void)setUp
{
    [super setUp];
    // Put setup code here. This method is called before the invocation of each
    // test method in the class.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of
    // each test method in the class.
    [super tearDown];
}

- (void)testForSummaryDataInitialization
{
    XCTAssertNotNil([PPLSummaryData shareInstance],
                    @"Instance of PPLSummaryData");
}

- (void)testForSummayRetuenedData
{
    PPLSummaryData *summaryData = [[PPLSummaryData alloc] init];
    [summaryData
        emotionValues:@"24hours"
             callBack:^(BOOL status, NSArray *graphValues, NSError *error) {
               if (status)
               {
                   XCTAssertGreaterThan(graphValues.count, 0,
                                        @"The count should be greater than 0");
                   XCTAssertNotNil(
                       [[graphValues objectAtIndex:0] objectForKey:@"emotion"],
                       @"The value of iconID should not be null");
               }
               else
               {
                   XCTAssertEqual(graphValues.count, 0,
                                  @"The count should be 0");
               }

             }];
}

- (void)testIfBarchartShowed
{
    PPLSummaryBarViewController *viewController =
        [[PPLSummaryBarViewController alloc] init];
    NSArray *viewArray = viewController.view.subviews;
    bool testSucceed = false;
    for (UIView *subview in viewArray)
    {

        NSString *subviewClass =
            [NSString stringWithFormat:@"%@", [subview class]];
        NSString *coreplotClass =
            [NSString stringWithFormat:@"%@", [CPTGraphHostingView class]];

        //if( [subview isKindOfClass:[CPTGraphHostingView class]])//stupid, this doesn't work
        if ([subviewClass isEqualToString:coreplotClass])
        {
            testSucceed = true;
            break;
        }
    }

    XCTAssertTrue(testSucceed);
}

- (void)testIfNotificationShowed
{
    PPLSummaryBarViewController *viewController =
        [[PPLSummaryBarViewController alloc] init];

    bool alertShowed = false;
    for (UIView *subview in viewController.view.subviews)
    {

        NSString *subviewClass =
            [NSString stringWithFormat:@"%@", [subview class]];
        NSString *coreplotClass =
            [NSString stringWithFormat:@"%@", [CSNotificationView class]];

        //if( [subview isKindOfClass:[CSNotificationView class]])//stupid,this doesn't work
        if ([subviewClass isEqualToString:coreplotClass])
        {
            alertShowed = true;
            break;
        }
    }

    if(viewController.shouldShowAlert)
    {
        XCTAssertTrue(alertShowed);
    }
    else
    {
        XCTAssertTrue(!alertShowed);
    }
}

@end
