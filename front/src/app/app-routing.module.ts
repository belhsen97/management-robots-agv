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
import { ChartsDashboardComponent } from './components/dashboard/charts-dashboard/charts-dashboard.component';
import { EditUserComponent } from './components/user/edit-user/edit-user.component';
import { EmailComposeComponent } from './components/email/email-compose/email-compose.component';
import { TableDashboardComponent } from './components/dashboard/table-dashboard/table-dashboard.component';
import { ListTagsComponent } from './components/workstation/list-tags/list-tags.component';
import { DetailsWorkstationComponent } from './components/workstation/details-workstation/details-workstation.component';
import { DetailsRobotComponent } from './components/robot/details-robot/details-robot.component';
import { TracingComponent } from './components/statistic/tracing/tracing.component';
import { StockChartComponent } from './components/dashboard/details/stock-chart/stock-chart.component';

const routes: Routes = [
  {
    path: 'statistic',
    component: LayoutComponent,
    children: [
      {
        path: 'tracing',
        component: TracingComponent
      }
    ]
  },
  {
    path: 'dashboard',
    component: LayoutComponent,
    children: [
      {
        path: 'table',
        component: TableDashboardComponent
      },
      {
        path: 'charts',
        component: ChartsDashboardComponent
      },
      {
        path: 'stock/:name',
        component: StockChartComponent
      }
    ]
  },
  {
    path: 'workstation',
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
    path: 'user',
    component: LayoutComponent,
    children: [
      {
        path: 'list',
        component: ListUsersComponent
      },
      {
        path: 'edit',
        component: EditUserComponent
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
    path: 'robot-avg',
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
    path: 'global',
    component: LayoutComponent,
    children: [
      {
        path: 'setting',
        component: GlobalSettingComponent
      },
      // {
      //   path: 'notification',
      //   component: NotificationComponent
      // }
    ]
  },
  {
    path: 'mail',
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