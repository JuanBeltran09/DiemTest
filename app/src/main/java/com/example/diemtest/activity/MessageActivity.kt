package com.example.diemtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.diemtest.R
import com.example.diemtest.databinding.ActivityMessageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView4.setOnClickListener{
            if (binding.yourMeassage.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your message",Toast.LENGTH_SHORT).show()
            }else{
                sendMessage(binding.yourMeassage.text.toString())
            }
        }
    }

    private fun sendMessage(msg: String) {

        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId = senderId+receiverId
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String,String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        FirebaseDatabase.getInstance().getReference("chats").child(chatId).setValue(map).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"Message Sended", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}