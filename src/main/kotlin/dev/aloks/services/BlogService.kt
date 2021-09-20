package dev.aloks.services

import dev.aloks.interfaces.BlogRepository
import dev.aloks.models.BlogRequest
import dev.aloks.models.BlogUpdateRequest
import dev.aloks.models.ServiceFunctionResponse

class BlogService: BlogRepository {
    override fun createBlog(blog: BlogRequest, username: String): ServiceFunctionResponse {
        TODO("Not yet implemented")
    }

    override fun getAllBlogs(): ServiceFunctionResponse {
        TODO("Not yet implemented")
    }

    override fun getBlogById(id: String): ServiceFunctionResponse {
        TODO("Not yet implemented")
    }

    override fun getBlogBySlug(slug: String): ServiceFunctionResponse {
        TODO("Not yet implemented")
    }

    override fun getUserBlogs(username: String): ServiceFunctionResponse {
        TODO("Not yet implemented")
    }

    override fun editBlog(slug: String, blog: BlogUpdateRequest) {
        TODO("Not yet implemented")
    }

    override fun deleteBlog(slug: String) {
        TODO("Not yet implemented")
    }
}