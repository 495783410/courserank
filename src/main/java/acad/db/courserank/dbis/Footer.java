package acad.db.courserank.dbis;

/**
 *
 * @author rishabh
 */
public class Footer {
    public Footer() {
        
    }
    
    @Override
    public String toString() {
        String result = "</div></div>";
        result += "<div id='footer' class='center'>";
        result += "<hr style='margin-top:30px; color:#fbfbfb;'/>";
        result += "&copy; All right reserved. Anvit, Rahul, Rishabh and Saif";
        result += "</div>";
        result += "</body> </html>";
        return result;
    }
}

/*
        </div>
    </div>

    <div id="footer" class="center">
        <hr style="margin-top:30px; color:#fbfbfb;"/>
        &copy; All right reserved. Anvit, Rahul, Rishabh and Saif
    </div>
</body>

</html>
 * 
 */