採用 MVVM/AAC 架構撰寫 主要組件：LiveData 、DataBinding、ViewModel、Repository(非Android提供) 架構邏輯：1.畫面生成採用 DataBinding 綁定，除方便調用視圖控件，也可以與XML進行數據響應數據操作 2.主要業務邏輯及需要被操作的 LiveData，皆放置在 ViewModel，因此每個頁面都會有屬於自己的ViewModel 3.Repository 作為資料存取介面，用於呼叫 API 的處理及本地資料庫(Room)的 CRUD 4.LiveData 用於響應任何資料變化到 UI 上的操作，如: API Callback 的資料響應到 UI 上

其他提要： 1.支持遊憩景觀加載更多功能(Data LoadMore) 2.SharePerference 本地輕量化儲存，專案中引用 AES-256 加密方式，確保資料安全 3.支援螢幕轉向功能 4.多國語系(繁體中文、簡體中文、英文、日語、韓語、西班牙語、泰語、越南文)

自研專案：「油價小精靈」，這是一款彙整台灣各廠牌油價及所有加油站等資訊的 App，採用 MVVM 架構撰寫，內含更多好用的
建構模組，目前以 Java 撰寫，正在改為 Kotlin https://github.com/JokingSun30/OilFairy.git
