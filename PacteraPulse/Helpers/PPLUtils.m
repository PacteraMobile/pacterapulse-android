//
//  PPLUtils.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLUtils.h"
#import <UIKit/UIKit.h>

@implementation PPLUtils

/**
 *  Method to get singleton instance for PPUtils class
 *
 *  @return a new created instance or an existing one
 */
+ (PPLUtils *)sharedClient
{
    static PPLUtils *_sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken,
                  ^{
        _sharedClient = [[PPLUtils alloc] init];
    });
    
    return _sharedClient;
}

/**
 *  Function to get unique ID for this device, this is used for submitting feedback to the server
 *
 *  @return unique ID for this device
 */
- (NSString*)getUniqueId
{
    return [[[UIDevice currentDevice] identifierForVendor] UUIDString];
}

-(PPLUtils *)sharedInstance
{
    return [PPLUtils sharedClient];
}

-(NSUserDefaults *)getStandardUserDefaults
{
    return [NSUserDefaults standardUserDefaults];
}
@end
