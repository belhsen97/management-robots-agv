import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { HeaderComponent } from './components/shared/header/header.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { PageErrorComponent } from './components/shared/page-error/page-error.component';
import { LayoutComponent } from './components/layout/layout.component';
// import { HighchartsChartModule } from 'highcharts-angular';
import { SignInComponent } from './components/user/sign-in/sign-in.component';
import { ListUsersComponent } from './components/user/list-users/list-users.component';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { StoreRouterConnectingModule } from '@ngrx/router-store';
import {   DashbordRouterSerializer } from './core/store/routers/DashbordRouterSerializer';
import { MaterialModule } from './Material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ListRobotsComponent } from './components/robot/list-robots/list-robots.component';
import { GlobalSettingComponent } from './components/setting/global-setting/global-setting.component';
import { ListWorkstationsComponent } from './components/workstation/list-workstations/list-workstations.component';
import { EditUserComponent } from './components/user/edit-user/edit-user.component';
import { SignUpComponent } from './components/user/sign-up/sign-up.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { FormForgotPasswordComponent } from './components/user/form-forgot-password/form-forgot-password.component';
import { GlobalEffects } from './core/store/effects/Global.Effects';
import { EffectsModule } from '@ngrx/effects';
import { SuccessSignUpComponent } from './components/user/success-sign-up/success-sign-up.component';
import { ChartsDashboardComponent } from './components/dashboard/charts-dashboard/charts-dashboard.component';
import { MessageBoxConfirmationComponent } from './components/shared/message-box-confirmation/message-box-confirmation.component';
import { MessageBoxUploadImgComponent } from './components/shared/message-box-upload-img/message-box-upload-img.component';
import { UpdateRoleComponent } from './components/user/update-role/update-role.component';
import { AddWorkstationComponent } from './components/workstation/add-workstation/add-workstation.component';
import { EmailComposeComponent } from './components/email/email-compose/email-compose.component';
import { TableDashboardComponent } from './components/dashboard/table-dashboard/table-dashboard.component';
import { PanelRobotComponent } from './components/shared/panel-robot/panel-robot.component';
import { AddTagComponent } from './components/workstation/add-tag/add-tag.component';
import { AddRobotComponent } from './components/robot/add-robot/add-robot.component';
import { CommonModule } from '@angular/common';
import { ListTagsComponent } from './components/workstation/list-tags/list-tags.component';
import { DetailsWorkstationComponent } from './components/workstation/details-workstation/details-workstation.component';
import { DetailsRobotComponent } from './components/robot/details-robot/details-robot.component';
import { TracingComponent } from './components/statistic/tracing/tracing.component';
import { IMqttServiceOptions, MqttModule } from 'ngx-mqtt';
import { environment } from 'src/environments/environment';
import { AppReducer } from './core/store/App.Reducer';
import { MqttEffects } from './core/store/effects/Mqtt.Effect';
import { GaugeChartComponent } from './components/dashboard/details/gauge-chart/gauge-chart.component';
import { StockChartComponent } from './components/dashboard/details/stock-chart/stock-chart.component';
import { RobotEffects } from './core/store/effects/Robot.Effect';


//export const MQTT_SERVICE_OPTIONS: IMqttServiceOptions = environment.mqttClientConfig;
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    SidebarComponent,
    PageErrorComponent,
    LayoutComponent,
    SignInComponent,
    ListUsersComponent,
    ListRobotsComponent,
    GlobalSettingComponent,
    ListWorkstationsComponent,
    EditUserComponent,
    SignUpComponent,
    FormForgotPasswordComponent,
    SuccessSignUpComponent,
    ChartsDashboardComponent,
    MessageBoxConfirmationComponent,
    MessageBoxUploadImgComponent,
    UpdateRoleComponent,
    AddWorkstationComponent,
    EmailComposeComponent,
    TableDashboardComponent,
    PanelRobotComponent,
    AddTagComponent,
    AddRobotComponent,
    ListTagsComponent,
    DetailsWorkstationComponent,
    DetailsRobotComponent,
    TracingComponent,
    GaugeChartComponent,
    StockChartComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule , 
    FormsModule,
    HttpClientModule,
    
    BrowserAnimationsModule,

    // HighchartsChartModule ,
    
    MaterialModule,
     
    StoreModule.forRoot(AppReducer , {
      runtimeChecks: {
        strictStateImmutability: false,
        strictActionImmutability: false,
      },
    }),
    StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: !isDevMode() }), 
    StoreRouterConnectingModule.forRoot({serializer : DashbordRouterSerializer}), BrowserAnimationsModule ,// include n number of reducer  / 25 max records clicking events 
    EffectsModule.forRoot([GlobalEffects, RobotEffects/*,MqttEffects*/]),

    
   MqttModule.forRoot(environment.mqttClientConfig)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
