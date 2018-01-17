package com.example.pill

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var medicineDatabaseHelper = MedicineDatabaseHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populate()

    }

    fun populate(){
        var data = medicineDatabaseHelper.data
        var listOfMedicine = ArrayList<Medicine>()

        while(data.moveToNext()){
            var name = data.getString(data.getColumnIndex("name"))
            var info = data.getString(data.getColumnIndex("info"))
            var inst = data.getString(data.getColumnIndex("instruction"))
            var photoLink = data.getString(data.getColumnIndex("photoLink"))

            var nextMedicine = Medicine(name, info, inst, photoLink)
            listOfMedicine.add(nextMedicine)
        }
        data.close()

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = MedicineAdapter(listOfMedicine, this)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater!!.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addBtn -> {
                    var addIntent = Intent(applicationContext, AddMedicineActivity::class.java)
                    startActivity(addIntent)
                }
                R.id.planBtn -> {
                    var intent = Intent(applicationContext, PlanActivity::class.java)
                    startActivity(intent)
                }
                R.id.settingsBtn -> {
                    var intent = Intent(applicationContext, PrefActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
