package ru.spbau.mit.dictionary

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_list_words.*
import ru.spbau.mit.dictionary.main.PagerAdapter
import android.view.MenuItem
import android.widget.Toast
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.SearchView
import ru.spbau.mit.dictionary.main.Word
import ru.spbau.mit.dictionary.study.StudyActivity
import android.content.ClipboardManager.OnPrimaryClipChangedListener



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val rootView = findViewById<View>(R.id.appbar)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
//        tabLayout.addTab(tabLayout.newTab().setText("На изучение"))
//        tabLayout.addTab(tabLayout.newTab().setText("Изученные"))
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


        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val mPrimaryChangeListener = OnPrimaryClipChangedListener {
            val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val item = clipBoard.primaryClip.getItemAt(0)
            if (item.text != null) {
                val intent = Intent(baseContext, AddWordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("word", item.text.toString())
                baseContext.startActivity(intent)
            }
        }
        clipboard.addPrimaryClipChangedListener(mPrimaryChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        //getting the search view from the menu
        val searchViewItem = menu!!.findItem(R.id.menuSearch)

        //getting search manager from systemservice
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        //getting the search view
        val searchView = searchViewItem.actionView as SearchView

        //you can put a hint for the search input field
        searchView.queryHint = "Поиск слов..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
        searchView.setIconifiedByDefault(true)

        //here we will get the search query
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // TODO("do search here")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuAbout -> Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show()
            R.id.menuSettings -> Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show()
            R.id.menuTest -> {
                // TODO ("get 10 words from database")
                val words: ArrayList<Word> = ArrayList()
                words.add(Word("hello", arrayListOf(getString(R.string.text))))
                words.add(Word("name", arrayListOf(getString(R.string.text))))
                words.add(Word("dog", arrayListOf(getString(R.string.text))))
                words.add(Word("cat", arrayListOf(getString(R.string.text))))
                words.add(Word("house", arrayListOf(getString(R.string.text))))
                // ==================================
                val intent = Intent(this, StudyActivity::class.java)
                intent.putExtra("words", words)
                startActivity(intent)
                //                Toast.makeText(this, "You clicked test", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.util.Log
//import android.view.View
//
//
//class MainActivity : AppCompatActivity(), View.OnClickListener {
//
//    private var globalContext: GlobalContext? = null
//    private val REQUEST_CODE = 1
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
//            globalContext!!.setLanguage(data)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        Log.d("OnCreate", "Start")
//        globalContext = GlobalContext(this, this)
//        globalContext!!.onCreate()
//    }
//
//    override fun onClick(v: View) {
//        globalContext!!.startActivityForResult(SearchActivity::class.java, REQUEST_CODE)
//    }
//}