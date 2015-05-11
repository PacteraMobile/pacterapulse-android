//
//  PPLColoredBarChart.h
//  PacteraPulse
//
//  Created by Randy.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PlotItem.h"

@interface PPLColoredBarChart : PlotItem<CPTPlotSpaceDelegate,
                                      CPTPlotDataSource,
                                      CPTBarPlotDelegate>

@property (nonatomic, weak)   NSArray * summaryData;

@end
