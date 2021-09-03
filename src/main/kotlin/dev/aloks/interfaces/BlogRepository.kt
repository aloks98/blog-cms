package dev.aloks.interfaces

interface BlogRepository {
    fun createBlog()
    fun getAllBlogs()
    fun getBlogById()
    fun getUserBlogs()
    fun editBlog()
    fun deleteBlog()
}