//
//  AppDelegate.h
//  PacteraPulse
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <UIKit/UIKit.h>

extern NSString *const kLauchFirstTime;
extern NSString *const kStoryboardId;
extern NSString *const kLaunchScreen;


@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

-(BOOL)checkFirstLaunch;
-(void)markFirstLaunch;
-(UIViewController*)selectRootViewController;


@end

