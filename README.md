# Novel

小说爬虫

![效果](docx.png)

## 使用的语言

Kotlin

## 使用的库

Selenium，Log4j，Apache POI

## 使用方式

1. 获取小说的各卷、章节的信息

```kotlin
// in Main.kt
fun getJson(id: Int, filename: String)
```

- id: 小说的id
- filename: 保存的路径

运行结果：

(**温馨提示：有的章节url为“javascript:cid(0)”，需手动修改**)

```json5
{
  "id": 8,
  "name": "欢迎来到实力至上主义的教室",
  "volumes": [
    {
      "name": "第一卷",
      "chapters": [
        {
          "name": "插图",
          "url": "https://www.linovelib.com/novel/8/114783.html"
        },
        // ......
      ]
    }
    // ......
  ]
}
```


2. 爬取为Word文档

```kotlin
// in Main.kt
fun getDocx(filename: String)
```

- filename: 上面保存的Json路径