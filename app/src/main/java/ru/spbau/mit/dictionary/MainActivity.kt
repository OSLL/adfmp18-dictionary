package ru.spbau.mit.dictionary

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import ru.spbau.mit.dictionary.main.PagerAdapter
import ru.spbau.mit.dictionary.study.StudyActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        //getting the search view from the menu
//        val searchViewItem = menu!!.findItem(R.id.menuSearch)
//
//        //getting search manager from system telservice
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//
//        //getting the search view
//        val searchView = searchViewItem.actionView as SearchView
//
//        //you can put a hint for the search input field
//        searchView.queryHint = "Поиск слов..."
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//
//        //by setting it true we are making it iconified
//        //so the search input will show up after taping the search iconified
//        //if you want to make it visible all the time make it false
//        searchView.setIconifiedByDefault(true)
//
//        //here we will get the search query
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // TODO("do search here")
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
//            R.id.menuAbout -> Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show()
//            R.id.menuSettings -> Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show()
            R.id.menuTest -> {
                val intent = Intent(this, StudyActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}