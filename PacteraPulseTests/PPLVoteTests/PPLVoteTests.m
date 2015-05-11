//
//  PacteraPulseTests.m
//  PacteraPulseTests
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "PPLVoteData.h"
@interface PPLVoteTests : XCTestCase

@end

@implementation PPLVoteTests

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

- (void)testFeedbackSubmitted
{
    PPLVoteData *dataModel = [PPLVoteData shareInstance];
    XCTestExpectation *completionExpectation = [self expectationWithDescription:@"Checkin Submitted Fetch"];
    
    [dataModel sendFeedback:[NSString stringWithFormat:@"%d", (rand()%3 +1 )]
                   callBack:^(BOOL status, NSString* serverResponse, NSError *error)
     {
         XCTAssertTrue(status);
         [completionExpectation fulfill];
     }];
    [self waitForExpectationsWithTimeout:5.0 handler:nil];
}

@end
