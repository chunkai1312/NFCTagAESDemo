# NFCTagAESDemo
103-1 NTUST RFID資訊安全 Programming Homework #2

## Requirements
以第一次作業為基礎，選用一種對稱式加密演算法 (DES/3DES/AES) 實作加密與解密功能：

### Reading Tag
* 可讀出 Tag 的類型與最大儲存容量 (Bytes)。
* 可讀出 Tag 的儲存內容 (TNF_WELL_KNOWN with RTD_TEXT)。
* 讀出的密文 (cipher text) 可經由解密後產生明文 (plain text)。

### Writing Tag
* 將文字訊息經過加密後建立 TNF_WELL_KNOWN with RTD_TEXT 型態的 Record。
* 將建立的 Record 包裝為 NDEF Message 然後寫入 Tag。