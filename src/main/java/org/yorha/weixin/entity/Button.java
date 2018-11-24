package org.yorha.weixin.entity;

public class Button {
//菜单类型
private String type;
//菜单名称
private String name;
//二级菜单
private Button[] sub_button;
public String getType() {
return type;
}
public void setType(String type) {
this.type = type;
}
public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public Button[] getSub_button() {
return sub_button;
}
public void setSub_button(Button[] sub_button) {
this.sub_button = sub_button;
}

}
--------------------- 
作者：Love_In_September 
来源：CSDN 
原文：https://blog.csdn.net/qq_38723394/article/details/79367798 
版权声明：本文为博主原创文章，转载请附上博文链接！