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
@interface PPLSummaryTests : XCTestCase

@end

@implementation PPLSummaryTests

- (void)setUp
{
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testForSummaryDataInitialization
{
  XCTAssertNotNil([PPLSummaryData shareInstance],@"Instance of PPLSummaryData");
}

- (void)testForSummayRetuenedData
{
  PPLSummaryData *summaryData = [[PPLSummaryData alloc]init];
  [summaryData emotionValues:@"24hours" callBack:^(BOOL status, NSArray *graphValues, NSError *error)
    {
    if (status)
    {
      XCTAssertGreaterThan(graphValues.count,0,@"The count should be greater than 0");
      XCTAssertNotNil([[graphValues objectAtIndex:0] objectForKey:@"emotion"], @"The value of iconID should not be null");
    } else {
      XCTAssertEqual (graphValues.count,0,@"The count should be 0");
    }

  }];
  
}

@end
