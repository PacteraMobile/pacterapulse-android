//
//  PPLNetworkingHelper.m
//  PacteraPulse
//
//  Created by Qazi.
//  Copyright (c) 2015 Pactera. All rights reserved.
//

#import "PPLNetworkingHelper.h"
#import "AFNetworkActivityIndicatorManager.h"
#import "PPLUtils.h"

#ifdef DEBUG
static NSString* kServerBaseURL = @"http://pacterapulse-sit.elasticbeanstalk.com/";
#else
static NSString* kServerBaseURL = @"http://pacterapulse-sit.elasticbeanstalk.com/";
#endif

@interface PPLNetworkingHelper()

/**
 *  httpManager will be used for HTTP Communincations will be initialized once 
 *  with base url
 */
@property (nonatomic, strong) AFHTTPRequestOperationManager *httpManager;

@end

@implementation PPLNetworkingHelper

/**
 *  Returns the signletone instance of this class
 *
 *  @return creates and returns the instance or returns the already created 
 * instance
 */
+ (PPLNetworkingHelper *)sharedClient
{
    static PPLNetworkingHelper *_sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedClient = [[PPLNetworkingHelper alloc] init];
    });
    
    return _sharedClient;
}

/**
 *  Clas level initialized function
 */
+ (void)initialize
{
    __unused PPLNetworkingHelper *sharedClient = [PPLNetworkingHelper sharedClient];
}

/**
 *  Instance level initialization woud create and initialize httpManager based 
 *  on the base URL
 *
 *  @return returns the class instance
 */
- (id)init
{
    if(self = [super init])
    {
        NSURL *baseURL = [NSURL URLWithString:kServerBaseURL];
        self.httpManager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:baseURL];
        self.httpManager.operationQueue.maxConcurrentOperationCount = 2;
        self.httpManager.requestSerializer = [AFHTTPRequestSerializer serializer];
        [self.httpManager.reachabilityManager startMonitoring];
        self.httpManager.requestSerializer.timeoutInterval = DEFAULT_TIMEOUT;
        [self.httpManager.requestSerializer setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
        [[AFNetworkActivityIndicatorManager sharedManager] setEnabled:YES];
    }
    return self;
}

/**
 *  Function to do GET HTTP request
 *
 *  @param relativeURL URL which is relative to the base URL
 *  @param parameters  Parameters to pass to the server for URL call
 *  @param success     callback function to be called incase of a successfull HTTP Request
 *  @param failure     callback function to be called if the HTTP request fails for any reason
 *
 *  @return return the created Request operation
 */
- (AFHTTPRequestOperation *)GET:(NSString *)relativeURL parameters:(NSDictionary *)parameters
                        success:(void (^)(NSString *responseString, id responseObject))success
                        failure:(void (^)(NSString *responseString, NSError *error))failure
{
    AFHTTPRequestOperation *op = [self.httpManager GET:relativeURL
                                            parameters:parameters
                                               success:^(AFHTTPRequestOperation *operation, id responseObject)
                                  {
                                      success(operation.responseString, responseObject);
                                  }
                                               failure:^(AFHTTPRequestOperation *operation, NSError *error)
                                  {
                                      failure(operation.responseString, error);
                                  }];
    return op;
}
/**
 *  Function to do Post HTTP request
 *
 *  @param relativeURL URL relative to the base URL
 *  @param parameters  Parameters to pass to the POST request
 *  @param success     callback function to be called is the HTTP Request is successful
 *  @param failure     callback function to be called if the HTTP Request fails
 *
 *  @return returns the created HTTP Request operation
 */
- (AFHTTPRequestOperation *)POST:(NSString *)relativeURL parameters:(NSDictionary *)parameters
                         success:(void (^)(NSString *responseString, id responseObject))success
                         failure:(void (^)(NSString *responseString, NSError *error))failure
{
    AFHTTPRequestOperation *op = [self.httpManager POST:relativeURL
                                             parameters:parameters
                                                success:^(AFHTTPRequestOperation *operation, id responseObject)
                                  {
                                      success(operation.responseString, responseObject);
                                  }
                                                failure:^(AFHTTPRequestOperation *operation, NSError *error)
                                  {
                                      failure(operation.responseString, error);
                                  }];
    
    
    
    return op;
}

@end
