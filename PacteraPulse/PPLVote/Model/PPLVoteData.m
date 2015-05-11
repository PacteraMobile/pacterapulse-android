//
//  PPLVoteData.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLVoteData.h"
#import "PPLUtils.h"
#import "PPLNetworkingHelper.h"

@implementation PPLVoteData
/**
 *  Single instance for PPLVode data
 *
 *  @return will return the singleton for this class
 */
+ (PPLVoteData *)shareInstance
{
    static PPLVoteData *shareInstance;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        shareInstance = [[self alloc]init];
    });
    return shareInstance;
}

/**
 *  This method is called from the Viewcontroller to send the selected 
 *  emotion to the server, it also gets the unique id of the device
 *  and submits with the emotion
 *
 *  @param feedBackValue Feedback value can be 1,2,3 which is Happy, 
 *                       Neutral and Sad, it is converted to string to 
 *                       send as parameter
 *  @param callback      This is the call back function which would allow Controller to perform UI Actions on return
 */
- (void)sendFeedback:(NSString *)feedBackValue callBack:(void(^)(BOOL status, NSString *serverResponse, NSError *error))callback
{
    PPLNetworkingHelper *client = [PPLNetworkingHelper sharedClient];
    NSString *deviceID = [[PPLUtils sharedClient] getUniqueId];
    NSString *postURL = [NSString stringWithFormat:@"%@/%@/%@", kVoteUrl, deviceID, feedBackValue];
    //Call the post function of the network hepler and process callbacks
    [client POST:postURL parameters:nil success:^(NSString*responseString, id responseObject)
     {
         if (callback)
         {
            callback(YES, responseString, nil);
         }

     }
       failure:^(NSString *responseString, NSError *error)
     {
         if (callback)
         {
             callback(NO, responseString, error);
         }
     }];
}

@end
