//
//  STGuideViewController.h
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EAIntroView.h"

@interface STGuideViewController : UITabBarController<EAIntroDelegate,HttpRequestDelegate,UITabBarControllerDelegate>

@property (strong,nonatomic) HttpRequest *hPicRequest;

@end
