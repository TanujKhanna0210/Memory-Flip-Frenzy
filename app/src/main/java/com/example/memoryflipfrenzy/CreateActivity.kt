package com.example.memoryflipfrenzy

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryflipfrenzy.models.BoardSize
import com.example.memoryflipfrenzy.utils.EXTRA_BOARD_SIZE
import com.example.memoryflipfrenzy.utils.isPermissionGranted
import com.example.memoryflipfrenzy.utils.requestPermission

class CreateActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreateActivity"
        const val PICK_PHOTO_CODE = 128
        const val READ_EXTERNAL_PHOTOS_CODE = 891
        const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        const val MIN_GAME_NAME_LENGTH = 3
        const val MAX_GAME_NAME_LENGTH = 14
    }

    private lateinit var rvImagePicker : RecyclerView
    private lateinit var etGameName : EditText
    private lateinit var btnSave : Button

    private lateinit var adapter: ImagePickerAdapter

    private lateinit var boardSize : BoardSize
    private var numImagesRequired = -1
    private val chosenImageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        rvImagePicker = findViewById(R.id.rvImagePicker)
        etGameName = findViewById(R.id.etGameName)
        btnSave = findViewById(R.id.btnSave)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImagesRequired = boardSize.getNumPairs()
        supportActionBar?.title = "Choose Pics (0 / $numImagesRequired)"

        btnSave.setOnClickListener {
            saveDataToFirebase()
        }
        etGameName.filters = arrayOf(InputFilter.LengthFilter(MAX_GAME_NAME_LENGTH))
        etGameName.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                btnSave.isEnabled = shouldEnableSaveButton()
            }
        })

        adapter = ImagePickerAdapter(this, chosenImageUris, boardSize, object: ImagePickerAdapter.ImageClickListener{
            override fun onPlaceHolderClicked() {
                if(isPermissionGranted(this@CreateActivity, READ_PHOTOS_PERMISSION))
                    launchIntentForPhotos()
                else
                    requestPermission(this@CreateActivity, READ_PHOTOS_PERMISSION, READ_EXTERNAL_PHOTOS_CODE)
            }

        })
        rvImagePicker.adapter = adapter
        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == READ_EXTERNAL_PHOTOS_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchIntentForPhotos()
            } else {
                Toast.makeText(this, "In order to create a custom game, you need to provide access to your photos", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null) {
            Log.w(TAG, "Did not get data back from the launched activity, user likely cancelled flow.")
            return
        }
        val selectedUri = data.data  // If the application launched allows to select only one photo
        val clipData = data.clipData  // If the application launched allows selection of multiple photos

        if(clipData != null){
            Log.i(TAG, "clipData numImages = ${clipData.itemCount}: $clipData")
            for(i in 0 until clipData.itemCount) {
                val clipItem = clipData.getItemAt(i)
                if(chosenImageUris.size < numImagesRequired) {
                    chosenImageUris.add(clipItem.uri)
                }
            }
        }
        else if(selectedUri != null){
            Log.i(TAG, "data : $selectedUri")
            chosenImageUris.add(selectedUri)
        }
        adapter.notifyDataSetChanged()
        supportActionBar?.title = "Choose pics (${chosenImageUris.size} / $numImagesRequired)"
        btnSave.isEnabled = shouldEnableSaveButton()
    }

    private fun saveDataToFirebase() {
        TODO("Not yet implemented")
        Log.i(TAG, "saveDataToFirebase")
    }

    private fun shouldEnableSaveButton(): Boolean {
        // Check if we should enable the save button or not
        if(chosenImageUris.size != numImagesRequired) {
            return false
        }
        if(etGameName.text.isBlank()  || etGameName.text.length < MIN_GAME_NAME_LENGTH) {
            return false
        }
        return true
    }

    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Choose pics"), PICK_PHOTO_CODE)
    }
}