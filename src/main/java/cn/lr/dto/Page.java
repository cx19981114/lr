package cn.lr.dto;
import java.util.List;
public class Page<T> {   
	private int total;    // 总条数
	private int pageNum;     // 当前页
	private int pageSize;     // 每页数
	private List<T> list; // 结果集
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "Page [total=" + total + ", pageNum=" + pageNum + ", pageSize=" + pageSize + ", list=" + list + "]";
	}
 
	
}
