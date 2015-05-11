//
//  PPLVoteData.h
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PPLVoteData : NSObject
+ (PPLVoteData *)shareInstance;
- (void)sendFeedback:(NSString*)feedBackValue callBack:
    (void(^)(BOOL status, NSString* serverResponse, NSError *error)) callback;
@end
