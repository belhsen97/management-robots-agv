import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageErrorComponent } from './components/shared/page-error/page-error.component';
import { LayoutComponent } from './components/layout/layout.component';
import { SignInComponent } from './components/user/sign-in/sign-in.component';
import { ListUsersComponent } from './components/user/list-users/list-users.component';
import { ListRobotsComponent } from './components/robot/list-robots/list-robots.component';
import { GlobalSettingComponent } from './components/setting/global-setting/global-setting.component';
import { ListWorkstationsComponent } from './components/workstation/list-workstations/list-workstations.component';
import { SignUpComponent } from './components/user/sign-up/sign-up.component';
import { FormForgotPasswordComponent } from './components/user/form-forgot-password/form-forgot-password.component';
import { SuccessSignUpComponent } from './components/user/success-sign-up/success-sign-up.component';
import { EditUserComponent } from './components/user/edit-user/edit-user.component';
import { EmailComposeComponent } from './components/email/email-compose/email-compose.component';
import { TableDashboardComponent } from './components/dashboard/table-dashboard/table-dashboard.component';
import { ListTagsComponent } from './components/workstation/list-tags/list-tags.component';
import { DetailsWorkstationComponent } from './components/workstation/details-workstation/details-workstation.component';
import { DetailsRobotComponent } from './components/robot/details-robot/details-robot.component';
import { TracingComponent } from './components/statistic/tracing/tracing.component';
import { StockChartComponent } from './components/dashboard/details/stock-chart/stock-chart.component';
import { ChartsRobotsStatisticComponent } from './components/statistic/charts-robots-statistic/charts-robots-statistic.component';
import { FieldDashboardComponent } from './components/dashboard/field-dashboard/field-dashboard.component';
import { ListNotificationsComponent } from './components/notification/list-notifications/list-notifications.component';
import { ActivateRouteService } from './core/services/activate-route.service';

const routes: Routes = [
  {
    path: 'statistic',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [ 
      {
        path: 'charts',
        component: ChartsRobotsStatisticComponent
      }
    ]
  },
  {
    path: 'dashboard',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'table',
        component: TableDashboardComponent
      },
      {
        path: 'field',
        component: FieldDashboardComponent
      },
      {
        path: 'stock/:name',
        component: StockChartComponent
      }
    ]
  },
  {
    path: 'workstation',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'list',
        component: ListWorkstationsComponent
      },
      {
        path: 'tags',
        component: ListTagsComponent
      }
      ,
      {
        path: ':id',
        component: DetailsWorkstationComponent
      }
    ]
  },
  {
    path: 'user',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'list',
        component: ListUsersComponent
      },
      {
        path: 'edit',
        component: EditUserComponent
      },
      {
        path: 'tracing',
        component: TracingComponent
      }
    ]
  },
  {
    path: 'notifications' , canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: ListNotificationsComponent
      }
    ]
  },
  {
    path: 'page-error',
    component: PageErrorComponent,
  },
  {
    path: 'sign-in',
    component: SignInComponent,
  },
  {
    path: 'sign-up',
    component: SignUpComponent,
  },
  {
    path: 'forgot-password',
    component: FormForgotPasswordComponent
  },
  {
    path: 'success-sign-up/:email',
    component: SuccessSignUpComponent
  },
  {
    path: 'robot-avg', canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'list',
        component: ListRobotsComponent
      },
      {
        path: ':name',
        component: DetailsRobotComponent
      }
    ]
  },
  {
    path: 'global',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'setting',
        component: GlobalSettingComponent
      }
    ]
  },
  {
    path: 'mail',  canActivateChild : [ActivateRouteService], 
    component: LayoutComponent,
    children: [
      {
        path: 'compose',
        component: EmailComposeComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }