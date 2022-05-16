package ru.geekbrains.kotlin_lesson1.view.contentProvider

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.databinding.FragmentContentProviderBinding
import ru.geekbrains.kotlin_lesson1.utlis.REQUEST_CODE

class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Объяснение")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else explain()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        cursor?.let {
            for (i in 0 until it.count) {
                if (cursor.moveToPosition(i)) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val hasNumber =
                        cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    binding.containerForContact.addView(TextView(requireContext()).apply {
                        text = name
                        textSize = resources.getDimension(R.dimen.text_contact)
                    })
                    if (hasNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        cursorPhone?.let {
                            if (cursorPhone.moveToNext()) {
                                val number = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                binding.containerForContact.addView(TextView(requireContext()).apply {
                                    text = number
                                    textSize = resources.getDimension(R.dimen.text_contact_number)
                                    setOnClickListener() {
                                        Snackbar.make(
                                            this,
                                            "Осуществить вызов по мобильной сети?",
                                            LENGTH_INDEFINITE
                                        )
                                            .setAction("Да") { call(number) }.show()
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
        cursor?.close()
    }

    private fun call(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("Номер телефона:$number"))
        startActivity(intent)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ContentProviderFragment()
    }
}