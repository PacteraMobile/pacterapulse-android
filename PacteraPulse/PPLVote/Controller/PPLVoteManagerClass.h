//
//  PPLVoteManagerClass.h
//  PacteraPulse
//
//  Created by Qazi on 14/05/2015.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <Foundation/Foundation.h>

#define FEEDBACK_VALUE_KEY @"LastVoteValue"
#define FEEDBACK_DATE_KEY @"VoteDate"

@interface PPLVoteManagerClass : NSObject

+ (PPLVoteManagerClass *)sharedInstance;

-(void)recordVoteSubmission:(NSString*)value;

-(BOOL)checkIfVoteSubmittedToday;


@end
