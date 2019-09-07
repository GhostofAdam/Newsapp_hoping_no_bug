package com.example.myapplication.Utilities;

public class DataList
{
    private String pageSize;
    private int total;
    private News[] data;
    private String currentPage;

    public String getPageSize()
    {
        return this.pageSize;
    }
    public int getTotal()
    {
        return this.total;
    }
    public News[] getData() { return this.data; }
    public String getCurrentPage() { return this.currentPage; }
    public void setData()   { this.data = new News[0];}
}

