package com.example.checktasks.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.checktasks.R;
import com.example.checktasks.db.DBHelper;
import com.example.checktasks.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private RecyclerView rvTasks;

    private DBHelper dbHelper;
    private TaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTask = findViewById(R.id.etTask);
        btnAdd = findViewById(R.id.btnAdd);
        rvTasks = findViewById(R.id.rvTasks);

        dbHelper = new DBHelper(this);

        loadTasks();

        btnAdd.setOnClickListener(v -> {
            String taskTitle = etTask.getText().toString().trim();
            if (TextUtils.isEmpty(taskTitle)) {
                Toast.makeText(MainActivity.this, "Ingrese una tarea", Toast.LENGTH_SHORT).show();
                return;
            }
            Task task = new Task();
            task.setTitle(taskTitle);
            task.setDone(false);
            dbHelper.addTask(task);
            etTask.setText("");
            loadTasks();
        });
    }

    private void loadTasks() {
        taskList = dbHelper.getAllTasks();
        if (adapter == null) {
            adapter = new TaskAdapter(taskList, (task, isChecked) -> {
                task.setDone(isChecked);
                dbHelper.updateTask(task);
                loadTasks();
            });
            rvTasks.setLayoutManager(new LinearLayoutManager(this));
            rvTasks.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
