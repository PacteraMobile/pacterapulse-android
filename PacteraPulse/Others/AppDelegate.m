//
//  AppDelegate.m
//  PacteraPulse
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//  

#import "AppDelegate.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

NSString *const kLauchFirstTime = @"LauchFirstTime";
NSString *const kLaunchScreen = @"launchController";
NSString *const kVoteScreen = @"voteController";
NSString *const kStoryboardId = @"Main";


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    UINavigationController *rootViewControoler = [[UINavigationController alloc]initWithRootViewController:[self selectRootViewController]];
    [self markFirstLaunch];
    
    self.window.rootViewController = rootViewControoler;
    [self.window makeKeyAndVisible];
    return YES;
}



#pragma -mark CustomFunctions
/**
 *  Function to check the first launch of the screen
 *
 *  @return returns true if this is the first launch
 */
- (BOOL)checkFirstLaunch
{
    return ![[NSUserDefaults standardUserDefaults] boolForKey:kLauchFirstTime];
}

/**
 *  This function marks the first launch of app in user defaults
 */
- (void)markFirstLaunch
{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setBool:YES forKey:kLauchFirstTime];
    [prefs synchronize];
}
/**
 *  Function to decide which Viewcontroller to take on launch, this basically switches
 *  the start view based on the fact if user has launched the app before or not. On 
 *  first launch it will show launch screen with instructions, on every next launch it 
 *  will directly take the user to voting screen
 *
 *  @return return the appropriate viewcontroller
 */
- (UIViewController*)selectRootViewController
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:kStoryboardId bundle:nil];

    UIViewController *initViewController = [storyboard instantiateViewControllerWithIdentifier:[self checkFirstLaunch]?kLaunchScreen:kVoteScreen];
    
    return initViewController;
}



@end
