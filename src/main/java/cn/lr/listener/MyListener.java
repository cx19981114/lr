package cn.lr.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Session创建+" + arg0.getSession().getId());
		arg0.getSession().setAttribute("companyId", 2);
	}
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Session销毁+" + arg0.getSession().getId());
	}
}
