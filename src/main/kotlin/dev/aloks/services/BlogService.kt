package dev.aloks.services

import dev.aloks.interfaces.BlogRepository
import dev.aloks.models.*
import dev.aloks.plugins.BadRequestException
import dev.aloks.plugins.ForbiddenException
import dev.aloks.plugins.NotFoundException
import dev.aloks.plugins.UnauthorizedException
import dev.aloks.repository.*
import org.bson.types.ObjectId
import org.litote.kmongo.*
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

    override fun getAllBlogs(): MutableList<BlogResponse> {
        val blogs = blogCollection.find()
        val allBlogs: MutableList<BlogResponse> = mutableListOf()
        blogs.forEach {
            val user = userCollection.findOneById(it.created_by)!!
            val blogCreatedBy = BlogCreatedBy(user.first_name+ " " + user.last_name, user.username)
            val blog = BlogResponse(
                it._id.toHexString(),
                it.slug,
                it.series,
                it.introduction,
                it.content,
                it.published,
                it.featured,
                blogCreatedBy,
                it.updated_at.toString()
            )
            allBlogs.add(blog)
        }
        return allBlogs
    }

    override fun getBlogById(id: String): BlogResponse {
        val dbBlog = blogCollection.findOneById(ObjectId(id)) ?: throw NotFoundException("Blog not found.")
        val user = userCollection.findOneById(dbBlog.created_by)!!
        val blogCreatedBy = BlogCreatedBy(user.first_name + " " + user.last_name, user.username)
        return BlogResponse(
            dbBlog._id.toHexString(),
            dbBlog.slug,
            dbBlog.series,
            dbBlog.introduction,
            dbBlog.content,
            dbBlog.published,
            dbBlog.featured,
            blogCreatedBy,
            dbBlog.updated_at.toString()
        )
    }

    override fun getBlogBySlug(slug: String): BlogResponse {
        val dbBlog = blogCollection.findOne(Blog::slug eq slug) ?: throw NotFoundException("Blog not found.")
        val user = userCollection.findOneById(dbBlog.created_by)!!
        val blogCreatedBy = BlogCreatedBy(user.first_name + " " + user.last_name, user.username)
        return BlogResponse(
            dbBlog._id.toHexString(),
            dbBlog.slug,
            dbBlog.series,
            dbBlog.introduction,
            dbBlog.content,
            dbBlog.published,
            dbBlog.featured,
            blogCreatedBy,
            dbBlog.updated_at.toString()
        )
    }

    override fun getUserBlogs(username: String): MutableList<BlogResponse> {
        val user = userCollection.findOne(User::username eq username)!!
        val userBlogs: MutableList<BlogResponse> = mutableListOf()

        val blogCreatedBy = BlogCreatedBy(user.first_name+ " " + user.last_name, user.username)
        val blogs = blogCollection.find(Blog::created_by eq user._id)
        blogs.forEach {
            val blog = BlogResponse(
                it._id.toHexString(),
                it.slug,
                it.series,
                it.introduction,
                it.content,
                it.published,
                it.featured,
                blogCreatedBy,
                it.updated_at.toString()
            )
            userBlogs.add(blog)
        }
        return userBlogs
    }

    override fun editBlog(slug: String, blog: BlogUpdateRequest, username: String): ServiceFunctionResponse {
        val user = userCollection.findOne(User::username eq username)!!
        val dbBlog = blogCollection.findOne(Blog::slug eq slug) ?: throw NotFoundException("Blog not found.")
        if (dbBlog.created_by != user._id) { throw ForbiddenException() }
        blogCollection.updateOneById(dbBlog._id, blog, updateOnlyNotNullProperties = true)
        return ServiceFunctionResponse(true, "Blog updated successfully")
    }

    override fun deleteBlog(slug: String) {
        TODO("Not yet implemented")
    }
}