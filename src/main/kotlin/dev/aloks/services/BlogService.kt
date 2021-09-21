package dev.aloks.services

import dev.aloks.interfaces.BlogRepository
import dev.aloks.models.*
import dev.aloks.plugins.BadRequestException
import dev.aloks.plugins.NotFoundException
import dev.aloks.repository.*
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue
import org.litote.kmongo.updateOneById
import org.litote.kmongo.util.idValue
import org.slf4j.LoggerFactory

class BlogService: BlogRepository {
    private val userCollection = selectUserCollection()
    private val blogCollection = selectBlogCollection()
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun createBlog(blog: BlogRequest, username: String): ServiceFunctionResponse {
        val (slug, series, introduction, content, published, featured) = blog
        blogCollection.findOne(Blog::slug eq slug)?.let { throw BadRequestException("Slug already exists.") }
        val user = userCollection.findOne(User::username eq username)
            ?: throw NotFoundException("User not found.")
        val userBlogs = user.blogs

        val id = ObjectId()
        val newBlog = Blog(
            _id = id,
            slug = slug,
            series = series,
            introduction = introduction,
            content = content,
            published = published,
            featured = featured,
            created_by = user._id
        )
        blogCollection.insertOne(newBlog)

        userBlogs?.add(id.toHexString())
        userCollection.updateOneById(user._id, setValue(User::blogs, userBlogs))

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