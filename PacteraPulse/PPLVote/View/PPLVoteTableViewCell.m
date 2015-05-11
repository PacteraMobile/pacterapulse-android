//
//  PPLVoteTableViewCell.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLVoteTableViewCell.h"
@implementation PPLVoteTableViewCell

/**
 *  Setup the row elemenet based on the PPLVote object
 *
 *  @param rowElement row element with image and value for this row
 */
- (void)setUpRow:(PPLVoteRow *)rowElement
{
    [self.emotionButton setImage:rowElement.rowImage forState:UIControlStateNormal];
    [[self.emotionButton imageView] setContentMode:UIViewContentModeScaleAspectFit];
    self.emotionButton.tag = rowElement.imageTag;
}

@end
