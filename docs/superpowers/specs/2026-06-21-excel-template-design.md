# Excel 模版專案設計

**Goal:** 建立一個可擴充的 Java 8 / Spring Boot / Gradle Excel 報表模版專案，提供以 `SXSSFWorkbook` 組報表並回傳 `byte[]` 下載的 API。

**Architecture:** 採用單一 Spring Boot 應用程式，但在程式碼層做清楚模組化。Excel 組裝責任與 Web 下載責任分離：Excel 組裝只處理 workbook、sheet、欄位與樣式；服務層負責把報表資料轉成 `byte[]`；Controller 只處理 HTTP 下載回應。這樣可以先用最小成本建立模板，後續若需求增加再拆成多模組。

**Tech Stack:** Java 8, Spring Boot 2.7.x, Gradle, Apache POI (SXSSFWorkbook), Spring Web, Spring Validation, JUnit 5。

---

## 範圍

本專案先完成以下能力：

1. 產生一個 XLSX 報表。
2. 使用 `SXSSFWorkbook` 進行大量資料的串流輸出。
3. 透過 API 下載檔案，回傳 `byte[]`。
4. 提供可重用的報表組裝介面，方便後續新增不同報表。

不包含以下內容：

1. 不做前端頁面。
2. 不做上傳或讀取 Excel。
3. 不做複雜排程、權限、或多租戶設定。
4. 不做資料庫整合，先用範例資料驅動報表。

## 架構

專案採單一 Gradle 專案，Package 依職責分層：

- `com.example.exceltemplate.config`
  - Spring Bean 設定與共用設定類。
- `com.example.exceltemplate.web`
  - Controller 與 HTTP 下載回應。
- `com.example.exceltemplate.service`
  - 報表服務，負責整合資料與 Excel builder。
- `com.example.exceltemplate.report`
  - 報表定義、資料模型、報表組裝入口。
- `com.example.exceltemplate.report.excel`
  - `SXSSFWorkbook` 組裝、sheet 建立、列/欄位輸出、樣式處理。
- `com.example.exceltemplate.common`
  - 共用例外、必要時的回應模型。

這個切法的目的，是讓 Web 層不直接依賴 POI API；未來若要改成 PDF、CSV、或多個報表格式，只需要替換 report 層實作。

## 報表流程

1. Controller 接收下載請求。
2. Service 選擇對應報表組建器。
3. 報表組建器建立 `SXSSFWorkbook`，寫入標題列與資料列。
4. Workbook 透過 `ByteArrayOutputStream` 轉成 `byte[]`。
5. Service 回傳檔名與內容。
6. Controller 回傳 `ResponseEntity<byte[]>`，加上下載標頭。

## API 設計

### 下載範例報表

- `GET /api/reports/sample/download`

回應：

- `200 OK`
- `Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- `Content-Disposition: attachment; filename="sample-report.xlsx"`
- body 為 Excel `byte[]`

### 請求資料

第一版先做無參數下載，降低模板複雜度。後續若要加篩選條件，再把查詢參數放進 request DTO。

## Excel 組裝規則

第一版報表模板會支援以下能力：

1. 建立 workbook 與單一 sheet。
2. 寫入標題列。
3. 寫入範例資料列。
4. 以基本樣式區分標題與內容。
5. 正確清理 `SXSSFWorkbook` 暫存資源。

組裝邏輯會包成可替換的 builder 或 assembler，避免報表結構散落在 Service 裡。

## 錯誤處理

1. 若報表資料為空，仍可產生空報表或標題列報表，不讓 API 直接失敗。
2. 若 workbook 生成過程發生錯誤，轉成應用程式例外，由統一例外處理器回傳 500。
3. 若下載參數未來擴充為必要欄位，會先做 `@Valid` 驗證，避免進入 Excel 組裝流程才失敗。
4. 所有 workbook 都必須在 finally 區塊中關閉與 `dispose()`，避免暫存檔案殘留。

## 測試策略

會補三層測試：

1. Service 測試：驗證可以產生非空 `byte[]`，且 workbook 能被 POI 正常打開。
2. Excel builder 測試：驗證 sheet 名稱、標題列、資料列順序正確。
3. Web layer 測試：驗證下載 API 回傳正確的 `Content-Type`、`Content-Disposition` 與 body。

若第一版沒有接資料庫，測試資料會直接在測試中建立，避免外部依賴。

## Gradle

建置系統採 Gradle，原因是：

1. 專案起步成本低。
2. 對 Spring Boot 支援成熟。
3. 後續若要拆模組，Gradle 對多模組管理足夠直接。

Gradle 會先提供：

- Spring Boot plugin
- Java 8 source / target 相容設定
- Apache POI 依賴
- Spring Web 與 Validation 依賴
- JUnit 5 測試依賴

## 驗收標準

完成後需達成以下結果：

1. 執行應用程式後，可透過 `GET /api/reports/sample/download` 下載 XLSX。
2. 下載後的檔案可被 Excel 開啟。
3. 產出的檔案內至少有一個 sheet、標題列與資料列。
4. 單元測試與 Web 測試可通過。
5. 程式碼維持模組化，Web 與 Excel 組裝責任分離。

