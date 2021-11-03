实现步骤：
1:在webapps/myweb目录下新建对应页面:
  update.html 修改页面
  update_success.html 修改成功提示页面
  update_fail.html 修改失败提示页面
  no_user.html 查无无此用户提示页面
2:在servlets包中新建处理修改密码业务的类:
  UpdateServlet并实现service方法
3:修改ClientHandler的分支,若url请求地址为修改操作，
  则实例化UpdateServlet并调用其service方法