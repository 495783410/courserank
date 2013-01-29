package acad.db.courserank.dbis.search;

/**
 *
 * @author rishabh
 */

import acad.db.courserank.dbis.global;
import java.sql.*;
import java.util.*;
import org.json.simple.*;

public class mainSearch {
    
    public static String query(int table_id, String str, int start_index, int max_fetch) throws Exception
    {
        JSONObject result = new JSONObject();
        JSONArray entry_list = new JSONArray();
        Map temp = new LinkedHashMap();
        int max_count,count;
        int maxCount[] = new int[6];
        Connection Con = global.getConnection();
        switch (table_id)
        {
            case 0:{
                entry_list.clear();
                start_index/=6;
                max_fetch/=6;
                /* Course */
                maxCount[0] = Course.getResultCount(Con, str);
                /* Student */
                maxCount[1] = Student.getResultCount(Con, str);
                /* Instructor */
                maxCount[2] = Instructor.getResultCount(Con, str);
                /* Work */
                maxCount[3] = Work.getResultCount(Con, str);
                /* Project */
                maxCount[4] = Project.getResultCount(Con, str);
                /* Post */
                maxCount[5] = Post.getResultCount(Con, str);
                
                /*
                entry_list = Course.Query(Con,str,start_index,max_fetch);
                entry_list.addAll(Student.Query(Con,str,start_index,max_fetch));
                entry_list.addAll(Instructor.Query(Con,str,start_index,max_fetch));
                entry_list.addAll(Work.Query(Con,str,start_index,max_fetch));
                entry_list.addAll(Project.Query(Con,str,start_index,max_fetch));
                entry_list.addAll(Post.Query(Con,str,start_index,max_fetch));
                */
                max_count = 0;
                SortedSet<Pair> set = new TreeSet<Pair>();
                for(int ii=0; ii<6; ii++) {
                    set.add(new Pair(ii, maxCount[ii]));
                    max_count += maxCount[ii];
                }
                addResults(entry_list, set, Con, str, start_index, max_fetch);
                break;
            }
            case 1: {
                entry_list = Course.Query(Con,str,start_index,max_fetch);
                max_count = Course.getResultCount(Con, str);
                break;
            }
            case 2: {
                entry_list = Student.Query(Con,str,start_index,max_fetch);
                max_count = Student.getResultCount(Con, str);
                break;
            }
            case 3: {
                entry_list = Instructor.Query(Con,str,start_index,max_fetch);
                max_count = Instructor.getResultCount(Con, str);
                break;
            }
            case 4: {
                entry_list = Project.Query(Con,str,start_index,max_fetch);
                max_count = Project.getResultCount(Con, str);
                break;
            }
            case 5: {
                entry_list = Work.Query(Con,str,start_index,max_fetch);
                max_count = Work.getResultCount(Con, str);
                break;
            }
            case 6: {
                entry_list = Post.Query(Con,str,start_index,max_fetch);
                max_count = Post.getResultCount(Con, str);
                break;
            }
            default: {
             max_count =0;
             break;
            }
        }
        count = entry_list.size();
        temp.put("max_count", max_count);
        temp.put("count",count);
        temp.put("entries", entry_list);
        result.put("results", temp);
        Con.close();
        return result.toJSONString();
    }
    
    static void addResults(JSONArray entry_list, SortedSet<Pair> set, Connection Con, String str, int start_index, int max_fetch) throws Exception {
        int leftOver = 0;
        int pleftOver = 0;
        int capacity;
        Pair temp;
        Iterator it = set.iterator();
        while(it.hasNext()) {
            temp = (Pair) it.next();
            capacity = temp.getCount();
            
            if (start_index == capacity) {
                leftOver += max_fetch;
                System.out.println("Case1: leftOver:"+leftOver + " start_index:" + start_index + "leftOver:" + leftOver);
            } else if((start_index+pleftOver) > capacity) {
                //No need to query this table
                pleftOver = (start_index+pleftOver-capacity);
                leftOver += max_fetch;
                System.out.println("Case2: leftOver:"+leftOver + " start_index:" + start_index + "leftOver:" + leftOver);
            } else if((start_index+pleftOver+max_fetch+leftOver) >= capacity) {
                addEntries(entry_list, temp.getId(), Con, str, start_index+pleftOver, leftOver+max_fetch);
                leftOver = (start_index+pleftOver+max_fetch+leftOver-capacity);
                pleftOver = 0;
                System.out.println("Case3: leftOver:"+leftOver + " start_index:" + start_index + "leftOver:" + leftOver);
            } else {
                addEntries(entry_list, temp.getId(), Con, str, start_index+pleftOver, leftOver+max_fetch);
                leftOver = 0;
                pleftOver = 0;
                System.out.println("Case4: leftOver:"+leftOver + " start_index:" + start_index + "leftOver:" + leftOver);
            }
        }
    }
    
    static void addEntries(JSONArray entry_list, int index, Connection Con, String str, int start_index, int max_fetch) throws Exception {
        switch(index) {
            case 0: {
               entry_list.addAll(Course.Query(Con,str,start_index,max_fetch));
                break;
            }
            case 1: {
                entry_list.addAll(Student.Query(Con,str,start_index,max_fetch));
                break;
            }
            case 2: {
                entry_list.addAll(Instructor.Query(Con,str,start_index,max_fetch));
                break;
            }
            case 3: {
                entry_list.addAll(Work.Query(Con,str,start_index,max_fetch));
                break;
            }
            case 4: {
                entry_list.addAll(Project.Query(Con,str,start_index,max_fetch));
            }
            case 5: {
                entry_list.addAll(Post.Query(Con,str,start_index,max_fetch));
            }
        }
    }
}

class Pair implements Comparable {

  private final int left;
  private final int right;

  public Pair(int left, int right) {
    this.left = left;
    this.right = right;
  }

  public int getId() { return left; }
  public int getCount() { return right; }

    @Override
    public int compareTo(Object t) {
        Pair p = (Pair) t;
        int retval;
        if(right >= p.right) {
            return 1;
        } else if(right == p.right) {
            return 0;
        } else {
            return -1;
        }
    }
  
}
