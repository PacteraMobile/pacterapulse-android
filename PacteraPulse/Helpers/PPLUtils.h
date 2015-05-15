//
//  PPLUtils.h
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PPLUtils : NSObject
/**
 *  Method to get singleton instance for PPUtils class
 *
 *  @return a new created instance or an existing one
 */
+ (PPLUtils *)sharedClient;
/**
 *  Function to get unique ID for this device, this is used for submitting feedback to the server
 *
 *  @return unique ID for this device
 */
-(NSString*)getUniqueId;

-(NSUserDefaults *)getStandardUserDefaults;
@end
