package com.ruoyi.devops.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.*;

public class WordExportUtil {

    public static byte[] generateReport(Map<String, Object> summary, String beginTime, String endTime) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        XWPFParagraph titlePara = document.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setText("投资系统运维报告");
        titleRun.setBold(true);
        titleRun.setFontSize(18);
        
        XWPFParagraph periodPara = document.createParagraph();
        periodPara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun periodRun = periodPara.createRun();
        String period = "统计周期：";
        if (beginTime != null && !beginTime.isEmpty()) {
            period += beginTime + " 至 " + (endTime != null ? endTime : "至今");
        } else {
            period += "全部";
        }
        periodRun.setText(period);
        periodRun.setFontSize(11);
        periodRun.setColor("666666");
        
        XWPFParagraph timePara = document.createParagraph();
        timePara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun timeRun = timePara.createRun();
        timeRun.setText("生成时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        timeRun.setFontSize(10);
        timeRun.setColor("999999");
        
        document.createParagraph();
        
        addHeading(document, "一、概览统计", 14);
        int total = getInt(summary, "total");
        int closed = getInt(summary, "closed");
        int unclosed = getInt(summary, "unclosed");
        int overdue = getInt(summary, "overdue");
        int closeRate = getInt(summary, "closeRate");
        
        XWPFTable overviewTable = document.createTable(2, 6);
        overviewTable.setWidth("100%");
        setCellText(overviewTable, 0, 0, "总事项");
        setCellText(overviewTable, 0, 1, "已闭环");
        setCellText(overviewTable, 0, 2, "未闭环");
        setCellText(overviewTable, 0, 3, "逾期");
        setCellText(overviewTable, 0, 4, "闭环率");
        setCellText(overviewTable, 0, 5, "");
        setCellText(overviewTable, 1, 0, String.valueOf(total));
        setCellText(overviewTable, 1, 1, String.valueOf(closed));
        setCellText(overviewTable, 1, 2, String.valueOf(unclosed));
        setCellText(overviewTable, 1, 3, String.valueOf(overdue));
        setCellText(overviewTable, 1, 4, closeRate + "%");
        setCellText(overviewTable, 1, 5, "");
        
        document.createParagraph();
        
        addHeading(document, "二、按类型统计", 14);
        List<Map<String, Object>> typeStats = getList(summary, "typeStats");
        if (typeStats != null && !typeStats.isEmpty()) {
            XWPFTable typeTable = document.createTable(typeStats.size() + 1, 5);
            typeTable.setWidth("100%");
            setCellText(typeTable, 0, 0, "类型");
            setCellText(typeTable, 0, 1, "总数");
            setCellText(typeTable, 0, 2, "已闭环");
            setCellText(typeTable, 0, 3, "未闭环");
            setCellText(typeTable, 0, 4, "闭环率");
            for (int i = 0; i < typeStats.size(); i++) {
                Map<String, Object> row = typeStats.get(i);
                setCellText(typeTable, i + 1, 0, getString(row, "type"));
                setCellText(typeTable, i + 1, 1, String.valueOf(getInt(row, "total")));
                setCellText(typeTable, i + 1, 2, String.valueOf(getInt(row, "closed")));
                setCellText(typeTable, i + 1, 3, String.valueOf(getInt(row, "unclosed")));
                setCellText(typeTable, i + 1, 4, getInt(row, "closeRate") + "%");
            }
        }
        
        document.createParagraph();
        
        addHeading(document, "三、按优先级统计", 14);
        List<Map<String, Object>> priorityStats = getList(summary, "priorityStats");
        if (priorityStats != null && !priorityStats.isEmpty()) {
            XWPFTable priorityTable = document.createTable(priorityStats.size() + 1, 4);
            priorityTable.setWidth("100%");
            setCellText(priorityTable, 0, 0, "优先级");
            setCellText(priorityTable, 0, 1, "总数");
            setCellText(priorityTable, 0, 2, "已闭环");
            setCellText(priorityTable, 0, 3, "逾期");
            for (int i = 0; i < priorityStats.size(); i++) {
                Map<String, Object> row = priorityStats.get(i);
                setCellText(priorityTable, i + 1, 0, getString(row, "priority"));
                setCellText(priorityTable, i + 1, 1, String.valueOf(getInt(row, "total")));
                setCellText(priorityTable, i + 1, 2, String.valueOf(getInt(row, "closed")));
                setCellText(priorityTable, i + 1, 3, String.valueOf(getInt(row, "overdue")));
            }
        }
        
        document.createParagraph();
        
        addHeading(document, "四、逾期事项明细", 14);
        Map<String, Object> overdueSummary = getMap(summary, "overdueSummary");
        List<Map<String, Object>> overdueItems = overdueSummary != null ? getList(overdueSummary, "items") : null;
        if (overdueItems != null && !overdueItems.isEmpty()) {
            addDetailTable(document, overdueItems);
        } else {
            addText(document, "暂无逾期事项");
        }
        
        document.createParagraph();
        
        addHeading(document, "五、未闭环事项明细", 14);
        Map<String, Object> unclosedSummary = getMap(summary, "unclosedSummary");
        List<Map<String, Object>> unclosedItems = unclosedSummary != null ? getList(unclosedSummary, "items") : null;
        if (unclosedItems != null && !unclosedItems.isEmpty()) {
            addDetailTable(document, unclosedItems);
        } else {
            addText(document, "暂无未闭环事项");
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();
        return out.toByteArray();
    }
    
    private static void addHeading(XWPFDocument document, String text, int fontSize) {
        XWPFParagraph para = document.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(fontSize);
    }
    
    private static void addText(XWPFDocument document, String text) {
        XWPFParagraph para = document.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(text);
        run.setFontSize(11);
    }
    
    private static void addDetailTable(XWPFDocument document, List<Map<String, Object>> items) {
        XWPFTable table = document.createTable(items.size() + 1, 5);
        table.setWidth("100%");
        setCellText(table, 0, 0, "事项编号");
        setCellText(table, 0, 1, "事项标题");
        setCellText(table, 0, 2, "状态");
        setCellText(table, 0, 3, "负责人");
        setCellText(table, 0, 4, "计划完成");
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = items.get(i);
            setCellText(table, i + 1, 0, getString(item, "itemNo"));
            setCellText(table, i + 1, 1, getString(item, "title"));
            setCellText(table, i + 1, 2, getString(item, "status"));
            setCellText(table, i + 1, 3, getString(item, "ownerName"));
            setCellText(table, i + 1, 4, getString(item, "planFinishTime"));
        }
    }
    
    private static void setCellText(XWPFTable table, int row, int col, String text) {
        XWPFTableCell cell = table.getRow(row).getCell(col);
        cell.setText(text != null ? text : "");
    }
    
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> getList(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof List) {
            return (List<Map<String, Object>>) value;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }
    
    private static int getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }
    
    private static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? String.valueOf(value) : "";
    }
}
