//
//  PPLVoteTableViewCell.h
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PPLVoteRow.h"

@interface PPLVoteTableViewCell : UITableViewCell
@property (nonatomic, weak) IBOutlet UIButton *emotionButton;

-(void)setUpRow:(PPLVoteRow *)rowElement;
@end
