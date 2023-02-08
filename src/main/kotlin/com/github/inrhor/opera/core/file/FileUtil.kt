package com.github.inrhor.opera.core.file

import com.github.inrhor.opera.Opera
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

/**
 * 返回文件夹的内容
 *
 * @param child 路径
 * @param mkdirs 是否创建 example.yml
 */
fun getFile(child: String, mkdirs: Boolean, vararg example: String = arrayOf("example")): File {
    val file = File(Opera.plugin.dataFolder, child)
    if (!file.exists() && mkdirs) { // 如果 <child> 文件夹不存在就给示例配置
        example.forEach {
            Opera.resource.releaseResourceFile("$child/$it.yml", true)
        }
    }
    return file
}

fun getFileList(file: File): List<File> =
    mutableListOf<File>().let { files ->
        if (file.isDirectory) {
            file.listFiles()!!.forEach { files.addAll(getFileList(it)) }
        }else if (file.name.endsWith(".yml", true)) {
            files.add(file)
        }
        return@let files
    }