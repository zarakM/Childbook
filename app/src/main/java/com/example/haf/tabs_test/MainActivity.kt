package com.example.haf.tabs_test

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

/**
 * Created by HP on 2/12/18.
 */
class MainActivity: AppCompatActivity() {
    private var mSearchAction: MenuItem? = null
    private var isSearchOpened = false
    private var edtSeach: EditText? = null

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mSearchAction = menu.findItem(R.id.action_search)
        return super.onPrepareOptionsMenu(menu)
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout

        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    protected fun handleMenuSearch() {
        val action = supportActionBar //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action!!.setDisplayShowCustomEnabled(false) //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true) //show the title in the action bar

            //hides the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edtSeach!!.windowToken, 0)

            //add the search icon in the action bar

            mSearchAction!!.icon = getDrawable(R.drawable.ic_menu_search)

            isSearchOpened = false
        } else { //open the search entry

            action!!.setDisplayShowCustomEnabled(true) //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar)//add the custom view
            action.setDisplayShowTitleEnabled(false) //hide the title

            edtSeach = action.customView.findViewById<View>(R.id.edtSearch) as EditText //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach!!.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch()
                        return true
                    }
                    return false
                }

                private fun doSearch() {}
            })


            edtSeach!!.requestFocus()

            //open the keyboard focused in the edtSearch
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT)


            //add the close icon
            mSearchAction!!.icon = getDrawable(R.drawable.ic_close_search)

            isSearchOpened = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_settings -> return true
            R.id.action_search -> {
                handleMenuSearch()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
            val textView = rootView.findViewById<View>(R.id.section_label) as TextView
            textView.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }


    override fun onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch()
            return
        }
        super.onBackPressed()
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position){
                0 ->{
                    val tab = Timeline()
                    return tab
                }
                1 ->{
                    val tab = Tab4()
                    return tab
                }
                2 ->{
                    val tab = Tab3()
                    return tab
                }
                3 ->{
                    val tab = Tab4()
                    return tab
                }
                else -> return null
            }
            return null
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 4
        }
    }
}