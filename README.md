## RMDebugItem

使用 buildSrc 的方式添加了插件，去除 class 中方法的 lineNumer 和 localVariable 信息，从而减小 dex 文件中的 debug_item 信息，最终减小 apk 的体积。