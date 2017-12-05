package com.fx.base.mvvm;

/**
 * Created by cmlanche on 2017/12/5.
 * <p>
 * 自定义View
 * <p>
 * 要求：
 * 1. fxml文件的根节点必须为fx:root type="StackPane"
 * 2. 不能在fxml文件中设置Controller
 * <p>
 * 效果：
 * 1. 你可以在fxml文件中直接用标签使用，如<YourCustomView><YourCustomView/>
 * 2. 你可以直接new YourCustomView来创建一个View
 * 3. 你的CustomView对象已经是一个Node节点，可以用来表示界面
 */
public abstract class CustomView extends DefaultView {

    public CustomView() {
        loadAsRoot();
    }
}
