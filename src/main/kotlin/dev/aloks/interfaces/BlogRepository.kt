package dev.aloks.interfaces

import dev.aloks.models.BlogRequest
import dev.aloks.models.BlogResponse
import dev.aloks.models.BlogUpdateRequest
import dev.aloks.models.ServiceFunctionResponse

interface BlogRepository {
    fun createBlog(blog: BlogRequest, username: String): ServiceFunctionResponse
    fun getAllBlogs(): MutableList<BlogResponse>
    fun getBlogById(id: String): ServiceFunctionResponse
    fun getBlogBySlug(slug: String): ServiceFunctionResponse
    fun getUserBlogs(username: String): ServiceFunctionResponse
    fun editBlog(slug: String, blog: BlogUpdateRequest)
    fun deleteBlog(slug: String)
}