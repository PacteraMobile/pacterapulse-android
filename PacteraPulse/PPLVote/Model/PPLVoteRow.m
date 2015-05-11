//
//  PPLVoteRow.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLVoteRow.h"

@implementation PPLVoteRow
/**
 *  Initilization for this object with Image and tag(which would be the value sent to server)
 *
 *  @param image Image to display on the row
 *  @param tag   Tag or value related to this image which would be sent to server
 *
 */
- (id)initWithImage:(UIImage *)image Tag:(NSInteger)tag
{
    self = [super init];
    if (self)
    {
        _rowImage = image;
        _imageTag = tag;
    }
    return self;
}

/**
 *  Class level function which initializes all the objects and return in an Array
 *
 *  @return return the created array with images and values
 */
+ (NSArray *)initObjects
{
    NSArray *imageNames = [[NSArray alloc] initWithObjects:@"unhappy",
                           @"neutral", @"happy", nil];
    NSMutableArray *returnArray = [[NSMutableArray alloc] init];
    NSInteger tagCounter = imageNames.count - 1;
    for (NSString *item in imageNames) {
        [returnArray addObject:[[PPLVoteRow alloc]
                                initWithImage:[UIImage imageNamed:item]
                                Tag:tagCounter--]];
    }
    return returnArray;
}

@end
