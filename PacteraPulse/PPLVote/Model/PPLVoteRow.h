//
//  PPLVoteRow.h
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface PPLVoteRow : NSObject

@property (nonatomic) NSInteger imageTag;
@property (nonatomic, strong) UIImage *rowImage;

+ (NSArray *)initObjects;

@end
