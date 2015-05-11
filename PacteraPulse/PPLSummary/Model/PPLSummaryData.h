//
//  PPLSummaryData.h
//  PacteraPulse
//
//  Created by jin.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PPLSummaryData : NSObject

+ (PPLSummaryData *)shareInstance;

- (void)emotionValues:(NSString *)period callBack:(void(^)(BOOL status, NSArray * graphValues, NSError *error))callback;

@end
