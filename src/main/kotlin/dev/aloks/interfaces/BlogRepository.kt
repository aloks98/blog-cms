package dev.aloks.interfaces

import dev.aloks.models.BlogRequest
import dev.aloks.models.BlogResponse
import dev.aloks.models.BlogUpdateRequest
import dev.aloks.models.ServiceFunctionResponse

interface BlogRepository {
    fun createBlog(blog: BlogRequest, username: String): ServiceFunctionResponse
    fun getAllBlogs(): MutableList<BlogResponse>
    fun getBlogById(id: String): BlogResponse
    fun getBlogBySlug(slug: String): BlogResponse
    fun getUserBlogs(username: String): MutableList<BlogResponse>
    fun editBlog(slug: String, blog: BlogUpdateRequest, username: String): ServiceFunctionResponse
    fun deleteBlog(slug: String)
}