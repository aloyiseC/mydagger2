package laiwei.mydagger2.component;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import laiwei.mydagger2.App;
import laiwei.mydagger2.module.BuilderModule;
import laiwei.mydagger2.module.RespositoryModule;
import laiwei.mydagger2.module.ToolsModule;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,BuilderModule.class, ToolsModule.class, RespositoryModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App app);
        AppComponent build();
    }
    void inject(App app);
}
