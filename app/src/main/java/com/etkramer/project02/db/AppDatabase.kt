package com.etkramer.project02.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Product::class, UserProductEdge::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun userProductEdgeDao(): UserProductEdgeDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                // Build new db.
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "app-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                seedUsers(db)
                seedProducts(db)
                seedUserProductEdges(db)

                instance = db
                return db.also { instance = db }
            }

            return instance as AppDatabase
        }

        private fun seedUsers(db: AppDatabase) {
            // Delete any existing users
            db.userDao().deleteAll()

            // Insert seed users into db
            db.userDao().insert(User(0, false, "testuser1", "testuser1", 200))
            db.userDao().insert(User(0, true, "admin2", "admin2", 999))
        }

        private fun seedProducts(db: AppDatabase) {
            // Delete any existing products
            db.productDao().deleteAll()

            val products = arrayOf(
                Product(0, "Soup Can", "Introducing our Classic Tomato Soup Can: A comforting and timeless favorite, this soup is crafted with ripe, sun-kissed tomatoes, aromatic herbs, and a hint of cream for a rich, velvety texture. Packed with flavor, it's the perfect remedy for a chilly day", 5),
                Product(0, "Chair", "Experience both style and comfort with our Contemporary Accent Chair. This sleek and chic piece of furniture seamlessly blends modern aesthetics with plush cushioning, making it a perfect addition to any room. Its sturdy construction ensures durability", 25),
                Product(0, "Phone", "Meet our latest smartphone, the pinnacle of technology and design. This sleek and powerful device boasts cutting-edge features, from a brilliant high-definition display to a lightning-fast processor. Capture life's moments in stunning detail with its", 80),
                Product(0, "Apple", "Behold the crisp and juicy Red Delicious Apple. A true nature's masterpiece, this vibrant fruit boasts a brilliant red skin, enticing with a shiny allure. When you take that first bite, you'll discover a sweet, refreshing crunch, and a burst of natural go", 3),
                Product(0, "Energy Drink", "Introducing our high-octane Energy Drink, a powerhouse of refreshment and vitality. Packed with invigorating caffeine and an electrifying blend of vitamins and minerals, this beverage is your go-to source for an instant energy boost. With a bold and zesty", 4),
                Product(0, "T-Shirt", "Discover our Classic Cotton T-Shirt, the epitome of casual comfort and style. Crafted from premium, breathable cotton fabric, it's a wardrobe essential that feels soft against your skin. The versatile design and wide range of colors make it the perfect", 20),
                Product(0, "Glasses", "Experience clarity and style with our premium eyeglasses. These finely crafted frames are designed to enhance your vision while making a fashion statement. Whether you're looking for sleek, modern designs or timeless classics, our glasses offer a perfect", 60),
                Product(0, "Robot", "Meet our cutting-edge robot, a marvel of technology and innovation. This mechanical wonder is designed to assist and entertain with unparalleled precision. From its advanced sensors and AI-powered brain to its versatile limbs and interactive features, it", 400),
                Product(0, "Shoes", "Step into style and comfort with our latest pair of shoes. These meticulously crafted footwear pieces seamlessly blend fashion and function. With a sleek design and quality materials, they offer a perfect balance of elegance and durability. Whether you're", 40),
                Product(0, "Keyboard", "Introducing our state-of-the-art keyboard, the ultimate tool for digital productivity and creativity. This sleek and responsive keyboard is designed to elevate your typing experience, with tactile feedback that's both satisfying and efficient. With custom", 120),
                Product(0, "Pencil", "Meet the classic No. 2 Pencil, an iconic tool for both learning and creativity. Crafted with a smooth wooden barrel and a reliable graphite core, it's the ideal instrument for sketching, writing, and everything in between. Perfectly sharpened, it effortlessly", 1),
                Product(0, "Tractor", "Introducing our rugged and dependable tractor, the workhorse of the farm and construction site. With its powerful engine, robust frame, and versatile attachments, this machine is engineered for heavy-duty tasks. Whether you're plowing fields, moving earth", 800),
                Product(0, "Window Spray", "Introducing our Window Cleaner Spray, the secret to sparkling, streak-free glass and crystal-clear views. This powerful and efficient formula effortlessly cuts through dirt, smudges, and grime, leaving your windows glistening and spotless. With a quick sp", 10),
                Product(0, "Top Hat", "Step into elegance and timeless style with our Top Hat. This iconic accessory exudes sophistication and grace, making a bold statement wherever you go. Crafted from premium materials and meticulously designed, it features a tall crown and a wide brim, add", 45),
                Product(0, "Pencil Case", "Organize your writing essentials with our sleek and practical pencil case. Designed with style and functionality in mind, this compact case keeps your pens, pencils, erasers, and more neatly stored and easily accessible. With durable materials and a secure", 25),
                Product(0, "Gloves", "Discover warmth and style with our versatile pair of gloves. These handcrafted wonders are designed to keep your hands cozy during chilly days, featuring a soft and insulating interior that shields you from the cold. With a sleek and fashionable exterior", 20),
            )

            // Insert seed products into db
            products.shuffle()
            for (product in products) {
                db.productDao().insert(product)
            }
        }

        private fun seedUserProductEdges(db: AppDatabase) {
            // Delete any existing edges
            db.userProductEdgeDao().deleteAll()

            // Insert seed edges into db
            for (username in arrayOf("testuser1", "admin2")) {
                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 4")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 8")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 9")?.id ?: -1,
                    true))

                db.userProductEdgeDao().insert(UserProductEdge(0,
                    db.userDao().findByName(username)?.id ?: -1,
                    db.productDao().findByName("Product 2")?.id ?: -1,
                    false))
            }
        }
    }
}
