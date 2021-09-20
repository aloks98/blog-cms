package dev.aloks.services

import dev.aloks.interfaces.BlogRepository
import dev.aloks.models.*
import dev.aloks.plugins.BadRequestException
import dev.aloks.plugins.NotFoundException
import dev.aloks.repository.*
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

class BlogService: BlogRepository {
    private val userCollection = selectUserCollection()
    private val blogCollection = selectBlogCollection()

    override fun createBlog(blog: BlogRequest, username: String): ServiceFunctionResponse {
        val (slug, series, introduction, content, published, featured) = blog
        blogCollection.findOne(Blog::slug eq slug) ?.let { throw BadRequestException("Slug already exists.") }
        val user = userCollection.findOne(User::username eq username)
            ?: throw NotFoundException("User not found.")
        val newBlog = Blog(
            slug = slug,
            series = series,
            introduction = introduction,
            content = content,
            published = published,
            featured = featured,
            created_by = user._id
        )
        blogCollection.insertOne(newBlog)
        return ServiceFunctionResponse(true, "Blog successfully created")
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